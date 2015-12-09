import java.util.UUID

import components.DataAccess
import data.{Person, Relation}

import scala.concurrent.{Future, ExecutionContext}
import scala.util.Try

/**
  * Test data access implementation
  */
case class TestDataAccess(people: Seq[Person], relations: Seq[Relation]) extends DataAccess {

  /**
    * Returns a person from the data storage.
    * @param id Person identifier.
    * @param executionContext Execution context.
    * @return Person
    */
  override def getPerson(id: UUID)(implicit executionContext: ExecutionContext): Future[Option[Person]] =
    Future.successful( people.find( p => p.id == id ) )

  /**
    * Returns a relation from the data storage.
    * @param fromId From person identifier.
    * @param toId To person identifier.
    * @param executionContext Execution context.
    * @return Person
    */
  override def getRelation(fromId: UUID, toId: UUID)(implicit executionContext: ExecutionContext): Future[Option[Relation]] = ???

  /**
    * Saved a person to the data storage.
    * @param person Person to be saved.
    * @param executionContext Execution context.
    * @return
    */
  override def savePerson(person: Person)(implicit executionContext: ExecutionContext): Future[Try[Person]] = ???

  /**
    * Saved a relation to the data storage.
    * @param relation Relation to be saved.
    * @param executionContext Execution context.
    * @return
    */
  override def saveRelation(relation: Relation)(implicit executionContext: ExecutionContext): Future[Try[Relation]] = ???
}
