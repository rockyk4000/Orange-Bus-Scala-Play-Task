package models

import play.api.libs.json.Json

case class UserInfo(
    name: String,
    email: String
    )

object UserInfo{
    implicit val userInfoJson = Json.format[UserInfo]
}