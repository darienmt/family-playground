package controllers

import components.{DataAccessComponent, ExecutionContextComponent, SearchAccessComponent}
import dal.{DataAccessImp, SearchAccessImp}
import org.reactivecouchbase.play.PlayCouchbase
import play.api.Play.current

/**
  * Configutation trait for current implementation.
  */
trait CouchbaseElasticseachComponent extends ExecutionContextComponent with DataAccessComponent with SearchAccessComponent {

  override implicit val executionContext = PlayCouchbase.couchbaseExecutor

  val dataAccess = DataAccessImp

  val searchAccess = SearchAccessImp
}
