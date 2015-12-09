package components

import java.util.UUID

import data.{Person, Relation}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Search capabilities.
  */
trait SearchAccess {

  /**
    * Index a person.
    * @param person Person to index.
    * @return
    */
  def indexPerson( person: Person )( implicit executionContext: ExecutionContext ) : Future[Long]

  /**
    * Index a relation.
    * @param relation Relation to index.
    * @return
    */
  def indexRelation( relation: Relation )( implicit executionContext: ExecutionContext ) : Future[Long]

  /**
    * Search people by name.
    * @param name Name to be found.
    * @param from From number of records.
    * @param limit Max number of records returned.
    * @param executionContext Execution context.
    * @return
    */
  def searchPeopleByName( name: String, from: Integer, limit: Integer )( implicit executionContext: ExecutionContext ) : Future[Seq[Person]]


  /**
    * Finds all the relations from the provided identifier.
    * @param fromId From person identifier.
    * @param executionContext
    * @return
    */
  def findRelations(fromId: UUID)( implicit executionContext: ExecutionContext ) : Future[Seq[Relation]]

  /**
    * Finds relationType relations from the provided identifier.
    * @param fromId From person identifier.
    * @param relationType Relation type.
    * @param executionContext
    * @return
    */
  def findRelationsType(fromId: UUID, relationType: String )( implicit executionContext: ExecutionContext ): Future[Seq[Relation]]
}
