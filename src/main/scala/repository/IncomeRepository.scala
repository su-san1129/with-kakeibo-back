package work.withkakeibo
package repository

import model.Income

import org.joda.time.DateTime
import scalikejdbc.DB

trait IncomeRepository {
  def create(): Unit
  def findAll(): Seq[Income]
}

object IncomeRepositoryImpl {
  def incomeRepositoryImpl =
    new IncomeRepository {
      def create(): Unit = {
        DB.localTx { implicit session =>
          Income.create(1, DateTime.now(), 100)
        }
      }
      def findAll(): Seq[Income] = {
        DB.localTx { implicit session =>
          Income.findAll()
        }
      }
    }
}
