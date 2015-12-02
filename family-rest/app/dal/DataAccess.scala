package dal

import java.util.UUID

import data.{Relation, Person}
import org.reactivecouchbase.play.PlayCouchbase

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

import play.api.Play.current

/**
  * Data access class
  */
object DataAccess {


  implicit val bucket = PlayCouchbase.defaultBucket

  /**
    * Returns a person from the data storage.
    * @param id Person identifier.
    * @param executionContext Execution context.
    * @return Person
    */
  def getPerson(id: UUID)(implicit executionContext: ExecutionContext) : Future[Option[Person]] =
    bucket.get[Person]( id.toString )


  /**
    * Saved a person to the data storage.
    * @param person Person to be saved.
    * @param executionContext Execution context.
    * @return
    */
  def savePerson(person: Person)(implicit executionContext: ExecutionContext) : Future[Try[Person]] =
    bucket.set[Person](person.id.toString, person ) map {
      result => {
        if ( result.isSuccess ) {
          Success(person)
        } else {
          Failure(new Exception( result.getMessage ) )
        }
      }
    }



  /**
    * Returns a relation from the data storage.
    * @param fromId From person identifier.
    * @param toId To person identifier.
    * @param executionContext Execution context.
    * @return Person
    */
  def getRelation(fromId: UUID, toId : UUID )(implicit executionContext: ExecutionContext) : Future[Option[Relation]] =
    bucket.get[Relation]( Utils.combineIds( fromId, toId ) )

  /**
    * Saved a relation to the data storage.
    * @param relation Relation to be saved.
    * @param executionContext Execution context.
    * @return
    */
  def saveRelation(relation: Relation)(implicit executionContext: ExecutionContext) : Future[Try[Relation]] =
    bucket.set[Relation]( Utils.combineIds( relation.fromId, relation.toId ), relation ) map {
      result => {
        if ( result.isSuccess ) {
          Success(relation)
        } else {
          Failure(new Exception( result.getMessage ) )
        }
      }
    }


}
