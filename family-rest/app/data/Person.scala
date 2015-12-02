package data

import java.util.UUID

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Person( id: UUID, name: String )

object Person {
  implicit val personReads: Reads[Person] = (
      ( __ \ "id") .read[UUID] ~
      ( __ \ "name") .read[String]
    ) ( Person.apply _ )

  implicit val personWrites: Writes[Person] = (
      ( __ \ "id") .write[UUID] ~
      ( __ \ "name") .write[String]
    ) ( unlift( Person.unapply ))
}



