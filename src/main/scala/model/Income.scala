package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._

case class Income(
    id: Int,
    userId: Int,
    payAt: DateTime,
    income: Int,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit session: DBSession = Income.autoSession): Income =
    Income.save(this)(session)

  def destroy()(implicit session: DBSession = Income.autoSession): Int =
    Income.destroy(this)(session)

}

object Income extends SQLSyntaxSupport[Income] {

  override val tableName = "incomes"

  override val columns =
    Seq("id", "user_id", "pay_at", "income", "created_at", "updated_at")

  def apply(i: SyntaxProvider[Income])(rs: WrappedResultSet): Income =
    apply(i.resultName)(rs)
  def apply(i: ResultName[Income])(rs: WrappedResultSet): Income =
    new Income(
      id = rs.get(i.id),
      userId = rs.get(i.userId),
      payAt = rs.get(i.payAt),
      income = rs.get(i.income),
      createdAt = rs.get(i.createdAt),
      updatedAt = rs.get(i.updatedAt)
    )

  val i = Income.syntax("i")

  override val autoSession = AutoSession

  def find(
      id: Int
  )(implicit session: DBSession = autoSession): Option[Income] = {
    withSQL {
      select.from(Income as i).where.eq(i.id, id)
    }.map(Income(i.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Income] = {
    withSQL(select.from(Income as i)).map(Income(i.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Income as i))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[Income] = {
    withSQL {
      select.from(Income as i).where.append(where)
    }.map(Income(i.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[Income] = {
    withSQL {
      select.from(Income as i).where.append(where)
    }.map(Income(i.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Income as i).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      userId: Int,
      payAt: DateTime,
      income: Int,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): Income = {
    val generatedKey = withSQL {
      insert
        .into(Income)
        .namedValues(
          column.userId -> userId,
          column.payAt -> payAt,
          column.income -> income,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    Income(
      id = generatedKey.toInt,
      userId = userId,
      payAt = payAt,
      income = income,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[Income]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "userId" -> entity.userId,
        "payAt" -> entity.payAt,
        "income" -> entity.income,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into incomes(
      user_id,
      pay_at,
      income,
      created_at,
      updated_at
    ) values (
      {userId},
      {payAt},
      {income},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(
      entity: Income
  )(implicit session: DBSession = autoSession): Income = {
    withSQL {
      update(Income)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.payAt -> entity.payAt,
          column.income -> entity.income,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(
      entity: Income
  )(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Income).where.eq(column.id, entity.id) }.update
      .apply()
  }

}
