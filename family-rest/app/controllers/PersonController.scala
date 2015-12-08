package controllers

import java.util.UUID

import components.{FamilyTreeComponent, SearchAccessComponent, DataAccessComponent, ExecutionContextComponent}
import data.Person
import play.api.libs.json.{JsSuccess, JsError, Json}
import play.api.mvc.{Action, Controller}


import scala.concurrent.Future
import scala.util.{Success, Failure, Try}


/**
  * Person controller
  */
trait PersonController extends Controller {

  this: ExecutionContextComponent with DataAccessComponent with SearchAccessComponent with FamilyTreeComponent=>

  /**
    * Returns a person by name.
    * @param id Person id.
    * @return
    */
  def getPerson( id: String ) = Action.async {
    Try( {
      UUID.fromString( id )
    }) match {
      case Failure(e) => Future.successful( BadRequest(Json.obj("message" -> s"The provided id($id) is not an UUID")) )
      case Success( myId ) =>
        dataAccess.getPerson(myId) map {
          case Some(person) => Ok(Json.toJson(person))
          case None => NotFound(Json.obj("message" -> s"There is no person with id $id"))
        }
    }
  }

  /**
    * Save a person to the database.
    * @return
    */
  def savePerson = Action.async(parse.json) { req =>
    req.body.validate[Person] match {
      case err@JsError(_) => Future.successful( BadRequest(Json.obj("message" -> ("The provided request body has some errors :" + JsError.toFlatJson(err).toString() ) )) )
      case jsPerson: JsSuccess[Person] => {
        val person: Person = jsPerson.get
        for {
          result <- dataAccess.savePerson(person)
          indexResult <- searchAccess.indexPerson(person)
        } yield Ok( Json.obj( "Location" -> ("/people/" + person.id.toString) ))
      }
    }
  }

  /**
    * Search people by name.
    * @param name Name to be found.
    * @return
    */
  def getPeopleByName( name: String ) = Action.async {
    searchAccess.searchPeopleByName(name, 0, 100) map {
      people => Ok(Json.toJson(people))
    }
  }

  /**
    * Returns the family tree for the person id on maxDepth.
    * @param id Person identifier.
    * @param maxDepth Tree max depth.
    * @return
    */
  def getFamilyTree( id: String, maxDepth: Int) = Action.async {
    Try( {
      UUID.fromString( id )
    }) match {
      case Failure(e) => Future.successful( BadRequest(Json.obj("message" -> s"The provided id($id) is not an UUID")) )
      case Success( myId ) =>
        familyTreeLogic.buildTree(myId, maxDepth) map {
          tree => Ok(Json.toJson(tree))
        }
    }
  }

  /**
    * Returns the family tree for the person id on 1.
    * @param id
    * @return
    */
  def getFamilyTreeDefault( id : String ) = getFamilyTree(id, 1)

  def getPeopleInRelationAtDepth( id: String, relationType: String, depth: Int) = Action.async {
    Try( {
      UUID.fromString( id )
    }) match {
      case Failure(e) => Future.successful( BadRequest(Json.obj("message" -> s"The provided id($id) is not an UUID")) )
      case Success( myId ) =>
        familyTreeLogic.getRelationsAt(myId, relationType, depth) map {
          people => Ok(Json.toJson(people))
        }
    }
  }
}
