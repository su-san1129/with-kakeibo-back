package work.withkakeibo
package repository

import model.User

import scalikejdbc.DB

trait UserRepository {
  def createUser(): Unit
  def findAll(): Seq[User]
}

object UserRepository {
  val userRepositoryImpl = new UserRepository {

    override def createUser(): Unit = {
      DB.localTx { implicit session =>
        User.create("test", "test@test.com", "test")
      }
    }

    override def findAll(): Seq[User] = {
      DB.localTx { implicit session =>
        User.findAll()
      }
    }
  }
}

