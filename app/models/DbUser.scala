package models

case class DbUser(userID: String,
                   firstName: Option[String],
                   lastName: Option[String],
                   email: Option[String],
                   avatarUrl: Option[String],
                   activated: Boolean)
