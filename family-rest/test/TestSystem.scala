import java.util.UUID

import components._
import data.{Person, Relation}

import scala.concurrent.{Future, ExecutionContext}
import scala.util.Try


trait TestEnvironment extends FamilyTreeLogicImp with ExecutionContextComponent with DataAccessComponent with SearchAccessComponent {
  implicit override val executionContext = ExecutionContext.Implicits.global

}


