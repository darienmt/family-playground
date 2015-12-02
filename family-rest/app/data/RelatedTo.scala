package data

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Represent a relation to a person.
  */
case class RelatedTo( toId: UUID, relationType: String )

object RelatedTo {
  implicit val relatedToReads: Reads[RelatedTo] = (
      ( __ \ "toId" ).read[UUID] ~
      ( __ \ "relationType" ).read[String]
    ) ( RelatedTo.apply _ )

  implicit val relatedToWrites: Writes[RelatedTo] = (
      ( __ \ "toId") .write[UUID] ~
      ( __ \ "relationType" ).write[String]
    ) ( unlift( RelatedTo.unapply ))
}
