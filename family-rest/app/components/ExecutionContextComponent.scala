package components

import scala.concurrent.ExecutionContext

/**
  * Execution context component
  */
trait ExecutionContextComponent {

  implicit val executionContext : ExecutionContext
}
