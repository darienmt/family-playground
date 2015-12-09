import java.util.UUID

import components.SearchAccess
import data.{Person, Relation}

import scala.concurrent.{Future, ExecutionContext}

/**
  * Test search access
  */
case class TestSearchAccess(people: Seq[Person], relations: Seq[Relation]) extends SearchAccess {
  /**
    * Index a person.
    * @param person Person to index.
    * @return
    */
  override def indexPerson(person: Person)(implicit executionContext: ExecutionContext): Future[Long] = ???

  /**
    * Finds all the relations from the provided identifier.
    * @param fromId From person identifier.
    * @param executionContext
    * @return
    */
  override def findRelations(fromId: UUID)(implicit executionContext: ExecutionContext): Future[Seq[Relation]] =
    Future.successful( relations.filter( r => r.fromId == fromId) )

  /**
    * Finds relationType relations from the provided identifier.
    * @param fromId From person identifier.
    * @param relationType Relation type.
    * @param executionContext
    * @return
    */
  def findRelationsType(fromId: UUID, relationType: String )( implicit executionContext: ExecutionContext ): Future[Seq[Relation]] =
    Future.successful( relations.filter( r => r.fromId == fromId && r.relationType.equals(relationType)) )
  /**
    * Search people by name.
    * @param name Name to be found.
    * @param from From number of records.
    * @param limit Max number of records returned.
    * @param executionContext Execution context.
    * @return
    */
  override def searchPeopleByName(name: String, from: Integer, limit: Integer)(implicit executionContext: ExecutionContext): Future[Seq[Person]] = ???

  /**
    * Index a relation.
    * @param relation Relation to index.
    * @return
    */
  override def indexRelation(relation: Relation)(implicit executionContext: ExecutionContext): Future[Long] = ???
}


