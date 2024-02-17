package work.withkakeibo
package service.UserService

import model.{Income, User}

import work.withkakeibo.repository.IncomeRepositoryImpl.incomeRepositoryImpl

trait IncomeService {
  def create(): Unit
  def findAll(): Seq[Income]
}

object IncomeService {
  val incomeServiceImpl = new IncomeService {
    val incomeRepository = incomeRepositoryImpl

    def create(): Unit = {
      incomeRepository.create()
    }
    def findAll(): Seq[Income] = {
      incomeRepository.findAll()
    }
  }
}
