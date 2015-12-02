package controllers

import play.api.libs.json._
import play.api.mvc._

object Application extends Controller {

  def info = Action {
    Ok(Json.obj( "project" -> "family tree service" ))
  }

}
