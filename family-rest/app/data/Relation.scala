package data

import java.util.UUID
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Relation( fromId: UUID, relationType: String, toId: UUID )

/**
  * Represents a relation between two persons.
  */
object Relation {
  implicit val relationReads: Reads[Relation] = (
      ( __ \ "fromId" ).read[UUID] ~
      ( __ \ "relationType" ).read[String] ~
      ( __ \ "toId" ).read[UUID]
    ) ( Relation.apply _ )

  implicit val relationWrites: Writes[Relation] = (
      ( __ \ "fromId" ).write[UUID] ~
      ( __ \ "relationType" ).write[String] ~
      ( __ \ "toId") .write[UUID]
    ) ( unlift( Relation.unapply ))
}
