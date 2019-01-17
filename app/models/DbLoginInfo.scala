package models

case class DbLoginInfo(id: Option[Int],
                        providerId: String,
                        providerKey: String)