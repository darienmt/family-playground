package components

import java.util.UUID

import data.{FamilyTree, Person}

import scala.concurrent.{Future, ExecutionContext}

/**
  * Family tree logic definition.
  */
trait FamilyTreeLogic {


  /**
    * Returns all the relations at depth of the person provided.
    * @param id Provided identifier.
    * @param relationType Relation type.
    * @param depth Depth.
    */
  def getRelationsAt(id: UUID, relationType: String, depth: Int)( implicit executionContext: ExecutionContext ) : Future[Seq[Person]]

  /**
    * Builds the family tree for the person id with max depth
    * @param id Person identifier.
    * @param maxDepth Max depth
    * @param executionContext
    * @return
    */
  def buildTree(id: UUID, maxDepth: Int )(implicit executionContext: ExecutionContext ) : Future[FamilyTree]

}
