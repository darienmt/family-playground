import java.util.UUID

import data.{Person, Relation}
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Family tree specs
  */
class FamilyTreeLogicSpec extends FlatSpec with Matchers with ScalaFutures with TestEnvironment{

  val p1 = Person(UUID.randomUUID(),"P1")
  val p11 = Person(UUID.randomUUID(),"P11")
  val p12 = Person(UUID.randomUUID(),"P12")
  val p111 = Person(UUID.randomUUID(),"P111")
  val p112 = Person(UUID.randomUUID(),"P112")
  val p1111 = Person(UUID.randomUUID(),"P1111")
  val p1112 = Person(UUID.randomUUID(),"P1112")
  val p1121 = Person(UUID.randomUUID(),"P1121")

  val p1_R_p11 = Relation(p1.id,"R",p11.id)
  val p1_R_p12 = Relation(p1.id,"R",p12.id)
  val p11_R_p111 = Relation(p11.id,"R",p111.id)
  val p11_R_p112 = Relation(p11.id,"R",p112.id)
  val p111_R_p1111 = Relation(p111.id,"R",p1111.id)
  val p112_R_p1121 = Relation(p112.id,"R",p1121.id)

  val p111_D_p1112 = Relation(p111.id,"D",p1112.id)

  var people = Seq(p1, p11, p12, p111, p112, p1111, p1112, p1121 )
  val rRelations = Seq(p1_R_p11, p1_R_p12, p11_R_p111, p11_R_p112, p111_R_p1111, p112_R_p1121)
  val dRelations = Seq(p111_D_p1112)
  var relations = rRelations ++ dRelations

  val dataAccess = TestDataAccess(people,relations)
  val searchAccess = TestSearchAccess(people,relations)

  val timeoutWhenReady: Timeout = timeout(600.milliseconds)

  "P1 relations R with depth 1" should " be P11 and P12" in {

    val fList = getRelationsAt(p1.id, "R", 1)
    whenReady( fList, timeoutWhenReady ) {
      relations => {
        relations should contain(p11)
        relations should contain(p12)
        val size = relations.size
        size should be (2)
      }
    }
  }


  "P1 relations R with depth 2" should " be P111 and P112" in {

    val fList = getRelationsAt(p1.id, "R", 2)
    whenReady( fList, timeoutWhenReady) {
      relations => {
        relations should contain(p111)
        relations should contain(p112)
        val size = relations.size
        size should be (2)
      }
    }
  }

  "P1 relations R with depth 3" should " be P1111 and P1121" in {

    val fList = getRelationsAt(p1.id, "R", 3)
    whenReady( fList, timeoutWhenReady) {
      relations => {
        relations should contain(p1111)
        relations should contain(p1121)
        val size = relations.size
        size should be (2)
      }
    }
  }

  "P1 relations R with depth 4" should " be empty" in {

    val fList = getRelationsAt(p1.id, "R", 4)
    whenReady( fList, timeoutWhenReady) {
      relations => {
        val size = relations.size
        size should be (0)
      }
    }
  }

  "P1 relations X with depth 1" should " be empty" in {

    val fList = getRelationsAt(p1.id, "X", 1)
    whenReady( fList, timeoutWhenReady) {
      relations => {
        val size = relations.size
        size should be (0)
      }
    }
  }

  "P1 relations D with depth 3" should " be P1112" in {

    val fList = getRelationsAt(p1.id, "D", 3)
    whenReady( fList, timeoutWhenReady) {
      relations => {
        relations should contain(p1112)
        val size = relations.size
        size should be (1)
      }
    }
  }
}
