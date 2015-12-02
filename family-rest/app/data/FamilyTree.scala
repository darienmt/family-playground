package data

import java.util.UUID

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Family tree representation
  */
case class FamilyTree( treeStartId: UUID, maxDepth: Int, people: Seq[Person], relations: Seq[Relation])


object FamilyTree {

  implicit val familyTreeReads: Reads[FamilyTree] = (
        ( __ \ "treeStartId") .read[UUID] ~
        ( __ \ "maxDepth" ) .read[Int] ~
        ( __  \ "people" ) .read[Seq[Person]] ~
        ( __ \ "relations" ) .read[Seq[Relation]]
    ) ( FamilyTree.apply _ )

  implicit val familyTreeWrites: Writes[FamilyTree] = (
      ( __ \ "treeStartId" ) .write[UUID] ~
      ( __ \ "maxDepth" ) .write[Int] ~
      ( __  \ "people" ) .write[Seq[Person]] ~
      ( __ \ "relations" ) .write[Seq[Relation]]
    ) ( unlift( FamilyTree.unapply ))

}
