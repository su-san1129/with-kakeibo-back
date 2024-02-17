package work.withkakeibo
package service.UserService

import repository.UserRepository.userRepositoryImpl

import work.withkakeibo.model.User

trait UserService {
  def createUser(): Unit
  def findAll(): Seq[User]
}

object UserService {
  val userServiceImpl = new UserService {
    val userRepository = userRepositoryImpl

    override def createUser(): Unit = {
      userRepository.createUser()
    }

    override def findAll(): Seq[User] = {
      userRepository.findAll()
    }
  }
}
