package components

import java.util.UUID

import data.{FamilyTree, Person, Relation}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Encapsulate family tree calculation logic.
  */
trait FamilyTreeLogicImp extends FamilyTreeLogic {

  this: ExecutionContextComponent with DataAccessComponent with SearchAccessComponent =>

  /**
    * Returns all the relations at depth of the person provided.
    * @param id Provided identifier.
    * @param relationType Relation type.
    * @param depth Depth.
    */
  def getRelationsAt(id: UUID, relationType: String, depth: Int)( implicit executionContext: ExecutionContext ) : Future[Seq[Person]] = ???

  /**
    * Builds the family tree for the person id with max depth
    * @param id Person identifier.
    * @param maxDepth Max depth
    * @param executionContext
    * @return
    */
  def buildTree(id: UUID, maxDepth: Int )(implicit executionContext: ExecutionContext ) : Future[FamilyTree] = {
    val depth = if ( maxDepth <= 0 ) 1 else maxDepth
    personRelationAtDepth( id, depth ) map {
      results => {
          val ( people, relations ) = results
          FamilyTree(id, maxDepth, people, relations )
      }
    }
  }

  /**
    * Gets the person's relations at the provided depth.
    * @param id Person identifier.
    * @param depth Relation depth.
    * @param executionContext
    * @return
    */
  def personRelationAtDepth( id: UUID, depth: Int )( implicit executionContext: ExecutionContext ) : Future[(Seq[Person],Seq[Relation])] = depth match {
    case 0 => dataAccess.getPerson(id).map {
      case None => (Seq(), Seq())
      case Some( person ) => (Seq(person), Seq())
    }
    case _ =>  for {
      (people,relations) <- aPersonAndItsRelations(id)
      results <- peopleRelationAtDepth( relations.map(_.toId), depth)
    } yield unionPersonAndRelations( (people, relations), results )
  }

  /**
    * Gets the person's relations at the provided depth.
    * @param ids Person ids.
    * @param depth Relation depth.
    * @param executionContext
    * @return
    */
  def peopleRelationAtDepth( ids: Seq[UUID], depth: Int )( implicit executionContext: ExecutionContext ) : Future[(Seq[Person],Seq[Relation])] = ids match {
    case Seq() => Future.successful(Seq(), Seq())
    case Seq(h) =>  personRelationAtDepth(h, depth - 1 )
    case h :: tail => {
      for {
        headResult <- personRelationAtDepth(h, depth - 1 )
        tailResult <- peopleRelationAtDepth(tail, depth )
      } yield unionPersonAndRelations(headResult, tailResult)
    }
  }

  /**
    * Gets a person data and its relations.
    * @param id Person identifier.
    * @param executionContext
    * @return
    */
  def aPersonAndItsRelations( id: UUID )( implicit executionContext: ExecutionContext ) : Future[(Seq[Person],Seq[Relation])] =
    for {
      person <- dataAccess.getPerson(id) map { _.get }
      relations <- findRelations( person.id )
    } yield unionPersonAndRelations( (Seq(person), Seq()), relations)

  /**
    * Returns the union betweek to sets of people and relations without duplicates.
    * @param set1 First set of people and relations.
    * @param set2 Second set of people and relations.
    * @return Union between set1 and set2.
    */
  def unionPersonAndRelations (set1: (Seq[Person], Seq[Relation]), set2: (Seq[Person], Seq[Relation]) ) = {
    val (p1People, p1Relations) = set1
    val (p2People, p2Relations) = set2
    ( (p1People ++ p2People).distinct, (p1Relations ++ p2Relations).distinct )
  }

  /**
    * Find the relations of the person.
    * @param id Person identifier.
    * @param executionContext
    * @return
    */
  def findRelations( id: UUID)( implicit executionContext: ExecutionContext ) : Future[(Seq[Person],Seq[Relation])] =
    for {
      relations <- searchAccess.findRelations( id )
      people <- peopleByRelations(relations)
    } yield (people, relations )

  /**
    * Find people by their relations.
    * @param relations Relations.
    * @param executionContext
    * @return
    */
  def peopleByRelations( relations: Seq[Relation])( implicit executionContext: ExecutionContext ) : Future[Seq[Person]] =
    Future.sequence( relations.map( r => dataAccess.getPerson( r.toId )) ) map {
      people => people.filter(_.nonEmpty).map( p => p.get )
    }
}
