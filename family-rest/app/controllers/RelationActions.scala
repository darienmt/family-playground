package controllers

import java.util.UUID

import dal.{SearchAccess, DataAccess}
import data.{Relation, RelatedTo, Person}

import org.reactivecouchbase.play.PlayCouchbase

import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.util.{Try, Success, Failure}

import play.api.Play.current

/**
  * Relation operations.
  */
object RelationActions extends Controller{

  implicit val couchbaseExecutionContext = PlayCouchbase.couchbaseExecutor

  /**
    * Finds the relation between two persons.
    * @param fromId From person id.
    * @param toId To person id.
    * @return
    */
  def getRelation(fromId: String, toId: String) = Action.async {
    Try( { UUID.fromString( fromId ) } ) match {
      case Failure(e) => Future.successful( BadRequest(Json.obj("message" -> s"The provided id($fromId) is not an UUID")) )
      case Success(myFromId) => Try( { UUID.fromString( toId ) } ) match {
        case Failure(e) => Future.successful( BadRequest(Json.obj("message" -> s"The provided person related to ($toId) is not an UUID")) )
        case Success(myToId) => DataAccess.getRelation(myFromId, myToId ) map {
          case None => NotFound(Json.obj("message" -> s"There is no relation between $fromId and $toId"))
          case Some(relation) => Ok(Json.obj("relation" -> relation.relationType))
        }
      }
    }
  }

  /**
    * Save a relation between two people.
    * @param fromId From person identifier.
    * @return
    */
  def saveRelation(fromId: String) = Action.async(parse.json) { req =>
    Try( { UUID.fromString( fromId ) } ) match {
      case Failure(e) => Future.successful( BadRequest(Json.obj("message" -> s"The provided id($fromId) is not an UUID")) )
      case Success(myFromId) => req.body.validate[RelatedTo] match {
        case err@JsError(_) => Future.successful( BadRequest(Json.obj("message" -> ("The provided request body has some errors :" + JsError.toFlatJson(err).toString() ) )) )
        case jsPerson: JsSuccess[RelatedTo] => {
          val relatedTo = jsPerson.get
          val relation = Relation(myFromId, relatedTo.relationType, relatedTo.toId )
          for {
            result <- DataAccess.saveRelation(relation)
            indexResult <- SearchAccess.indexRelation(relation)
          } yield Ok( Json.obj( "Location" -> ("/people/" + fromId.toString + "/relatedto/" + relatedTo.toId.toString ) ))
        }
      }
    }
  }

}
