package dal

import java.util.UUID


object Utils {

  /**
    * Combines two ids.
    * @param fromId
    * @param toId
    * @return
    */
  def combineIds( fromId: UUID, toId: UUID ) = fromId.toString + "_" + toId.toString
}
