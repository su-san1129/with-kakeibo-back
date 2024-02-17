package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._

case class FixedCost(
    id: Int,
    userId: Int,
    name: String,
    price: Int,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit session: DBSession = FixedCost.autoSession): FixedCost =
    FixedCost.save(this)(session)

  def destroy()(implicit session: DBSession = FixedCost.autoSession): Int =
    FixedCost.destroy(this)(session)

}

object FixedCost extends SQLSyntaxSupport[FixedCost] {

  override val tableName = "fixed_costs"

  override val columns =
    Seq("id", "user_id", "name", "price", "created_at", "updated_at")

  def apply(fc: SyntaxProvider[FixedCost])(rs: WrappedResultSet): FixedCost =
    apply(fc.resultName)(rs)
  def apply(fc: ResultName[FixedCost])(rs: WrappedResultSet): FixedCost =
    new FixedCost(
      id = rs.get(fc.id),
      userId = rs.get(fc.userId),
      name = rs.get(fc.name),
      price = rs.get(fc.price),
      createdAt = rs.get(fc.createdAt),
      updatedAt = rs.get(fc.updatedAt)
    )

  val fc = FixedCost.syntax("fc")

  override val autoSession = AutoSession

  def find(
      id: Int
  )(implicit session: DBSession = autoSession): Option[FixedCost] = {
    withSQL {
      select.from(FixedCost as fc).where.eq(fc.id, id)
    }.map(FixedCost(fc.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[FixedCost] = {
    withSQL(select.from(FixedCost as fc))
      .map(FixedCost(fc.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(FixedCost as fc))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[FixedCost] = {
    withSQL {
      select.from(FixedCost as fc).where.append(where)
    }.map(FixedCost(fc.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[FixedCost] = {
    withSQL {
      select.from(FixedCost as fc).where.append(where)
    }.map(FixedCost(fc.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(FixedCost as fc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      userId: Int,
      name: String,
      price: Int,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): FixedCost = {
    val generatedKey = withSQL {
      insert
        .into(FixedCost)
        .namedValues(
          column.userId -> userId,
          column.name -> name,
          column.price -> price,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    FixedCost(
      id = generatedKey.toInt,
      userId = userId,
      name = name,
      price = price,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[FixedCost]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "userId" -> entity.userId,
        "name" -> entity.name,
        "price" -> entity.price,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into fixed_costs(
      user_id,
      name,
      price,
      created_at,
      updated_at
    ) values (
      {userId},
      {name},
      {price},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(
      entity: FixedCost
  )(implicit session: DBSession = autoSession): FixedCost = {
    withSQL {
      update(FixedCost)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.name -> entity.name,
          column.price -> entity.price,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(
      entity: FixedCost
  )(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(FixedCost).where.eq(column.id, entity.id) }.update
      .apply()
  }

}
