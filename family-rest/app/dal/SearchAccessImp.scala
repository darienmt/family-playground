package dal

import java.util.UUID

import com.sksamuel.elastic4s.{ElasticsearchClientUri, ElasticClient, HitAs, RichSearchHit}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.source.Indexable
import components.SearchAccess
import data.{Relation, Person}
import play.api.libs.json.Json
import play.api.Play

import scala.concurrent.{Future, ExecutionContext}
;

/**
  * Allows to search data.
  */
object SearchAccessImp extends SearchAccess {

  implicit object PersonIndexable extends Indexable[Person] {
    override def json(p: Person): String = Json.stringify( Json.toJson(p) )
  }

  implicit object PersonHitAs extends HitAs[Person] {
    override def as(hit: RichSearchHit): Person = {
      Person( UUID.fromString( hit.sourceAsMap("id").toString ), hit.sourceAsMap("name").toString)
    }
  }

  implicit object RelationIndexable extends Indexable[Relation] {
    override def json(r: Relation): String = Json.stringify( Json.toJson(r) )
  }

  implicit object RelationHitAs extends HitAs[Relation] {
    override def as(hit: RichSearchHit): Relation = {
      Relation( UUID.fromString( hit.sourceAsMap("fromId").toString ), hit.sourceAsMap("relationType").toString, UUID.fromString( hit.sourceAsMap("toId").toString ))
    }
  }

  val uri = ElasticsearchClientUri(Play.current.configuration.getString("elasticseach.urls").get)
  val client = ElasticClient.transport(uri)

  /**
    * Index a person.
    * @param person Person to index.
    * @return
    */
  def indexPerson( person: Person )( implicit executionContext: ExecutionContext ) : Future[Long]=
    client.execute {
      index into "family-tree" / "people" id person.id.toString source person
    } map {
      resp => resp.getVersion
    }

  /**
    * Index a relation.
    * @param relation Relation to index.
    * @return
    */
  def indexRelation( relation: Relation )( implicit executionContext: ExecutionContext ) : Future[Long] =
    client.execute {
      index into "family-tree" / "relations" id Utils.combineIds(relation.fromId, relation.toId) source relation
    } map {
      resp => resp.getVersion
    }

  /**
    * Search people by name.
    * @param name Name to be found.
    * @param from From number of records.
    * @param limit Max number of records returned.
    * @param executionContext Execution context.
    * @return
    */
  def searchPeopleByName( name: String, from: Integer, limit: Integer )( implicit executionContext: ExecutionContext ) : Future[Seq[Person]]=
    client.execute {
      search in "family-tree" / "people" start from limit limit sort ( field sort "name" ) rawQuery( "{ \"query_string\" : { \"query\" : \"name:*" + name +  "*\" } }" )
    } map {
      resp => resp.as[Person].toSeq
    }


  /**
    * Finds all the relations from the provided identifier.
    * @param fromId From person identifier.
    * @param executionContext
    * @return
    */
  def findRelations(fromId: UUID)( implicit executionContext: ExecutionContext ) =
    client.execute {
      search in "family-tree" / "relations" query termQuery( "fromId", fromId.toString )
    } map {
      resp => resp.as[Relation].toSeq
    }
}
