package infrastructure.persistence

import java.time.LocalTime
import java.util.UUID

import org.joda.time.DateTime

trait DoobieCustomMapping {

  implicit val localTimeMeta: doobie.Meta[LocalTime] = doobie
    .Meta[String]
    .timap(LocalTime.parse(_))(_.toString)

  implicit val uuidMeta: doobie.Meta[UUID] = doobie
    .Meta[String]
    .timap(UUID.fromString(_))(_.toString)

  implicit val dateTimeMeta: doobie.Meta[DateTime] = doobie
    .Meta[String]
    .timap( DateTime.parse(_) )(_.toString)

}
