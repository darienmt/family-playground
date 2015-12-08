package components

import java.util.UUID

import data.{Person, Relation}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

/**
  * Data access capabilities.
  */
trait DataAccess {

  /**
    * Returns a person from the data storage.
    * @param id Person identifier.
    * @param executionContext Execution context.
    * @return Person
    */
  def getPerson(id: UUID)(implicit executionContext: ExecutionContext): Future[Option[Person]]

  /**
    * Saved a person to the data storage.
    * @param person Person to be saved.
    * @param executionContext Execution context.
    * @return
    */
  def savePerson(person: Person)(implicit executionContext: ExecutionContext): Future[Try[Person]]


  /**
    * Returns a relation from the data storage.
    * @param fromId From person identifier.
    * @param toId To person identifier.
    * @param executionContext Execution context.
    * @return Person
    */
  def getRelation(fromId: UUID, toId: UUID)(implicit executionContext: ExecutionContext): Future[Option[Relation]]

  /**
    * Saved a relation to the data storage.
    * @param relation Relation to be saved.
    * @param executionContext Execution context.
    * @return
    */
  def saveRelation(relation: Relation)(implicit executionContext: ExecutionContext): Future[Try[Relation]]
}
