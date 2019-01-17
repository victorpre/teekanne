package services

import com.google.inject.Inject
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile
import infrastructure.persistence.repositories.UserRepository
import models.User

import scala.concurrent.{ExecutionContext, Future}


trait UserService extends IdentityService[User] {
  def save(user: User): Future[User]
//  def save(profile: CommonSocialProfile): Future[User]
}

class UserServiceImpl @Inject()(userRepository: UserRepository)(implicit ex: ExecutionContext) extends UserService {
  override def save(user: User): Future[User] = userRepository.save(user)

//  override def save(profile: CommonSocialProfile): Future[User] = ???

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userRepository.find(loginInfo)
}