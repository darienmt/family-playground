package controllers

import components.{FamilyTreeLogicImp, FamilyTreeComponent}
import data.FamilyTree
import play.api.libs.json._
import play.api.mvc._

object Application extends Controller {

  def info = Action {
    Ok(Json.obj( "project" -> "family tree service" ))
  }

}

object MyFamilyTree extends FamilyTreeLogicImp with CouchbaseElasticseachComponent

/**
  * Person operations
  */
object PersonActions extends PersonController with CouchbaseElasticseachComponent with FamilyTreeComponent {
  val familyTreeLogic = MyFamilyTree
}

/**
  * Relation operations.
  */
object RelationActions extends RelationController with CouchbaseElasticseachComponent