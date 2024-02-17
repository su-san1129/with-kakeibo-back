package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._

case class VariableCost(
    id: Int,
    userId: Int,
    categoryId: Int,
    price: Int,
    opinion: Option[String] = None,
    paymentAt: DateTime,
    address: Option[String] = None,
    latitude: Option[BigDecimal] = None,
    longitude: Option[BigDecimal] = None,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit
      session: DBSession = VariableCost.autoSession
  ): VariableCost = VariableCost.save(this)(session)

  def destroy()(implicit session: DBSession = VariableCost.autoSession): Int =
    VariableCost.destroy(this)(session)

}

object VariableCost extends SQLSyntaxSupport[VariableCost] {

  override val tableName = "variable_costs"

  override val columns = Seq(
    "id",
    "user_id",
    "category_id",
    "price",
    "opinion",
    "payment_at",
    "address",
    "latitude",
    "longitude",
    "created_at",
    "updated_at"
  )

  def apply(vc: SyntaxProvider[VariableCost])(
      rs: WrappedResultSet
  ): VariableCost = apply(vc.resultName)(rs)
  def apply(vc: ResultName[VariableCost])(rs: WrappedResultSet): VariableCost =
    new VariableCost(
      id = rs.get(vc.id),
      userId = rs.get(vc.userId),
      categoryId = rs.get(vc.categoryId),
      price = rs.get(vc.price),
      opinion = rs.get(vc.opinion),
      paymentAt = rs.get(vc.paymentAt),
      address = rs.get(vc.address),
      latitude = rs.get(vc.latitude),
      longitude = rs.get(vc.longitude),
      createdAt = rs.get(vc.createdAt),
      updatedAt = rs.get(vc.updatedAt)
    )

  val vc = VariableCost.syntax("vc")

  override val autoSession = AutoSession

  def find(
      id: Int
  )(implicit session: DBSession = autoSession): Option[VariableCost] = {
    withSQL {
      select.from(VariableCost as vc).where.eq(vc.id, id)
    }.map(VariableCost(vc.resultName)).single.apply()
  }

  def findAll()(implicit
      session: DBSession = autoSession
  ): List[VariableCost] = {
    withSQL(select.from(VariableCost as vc))
      .map(VariableCost(vc.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(VariableCost as vc))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[VariableCost] = {
    withSQL {
      select.from(VariableCost as vc).where.append(where)
    }.map(VariableCost(vc.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[VariableCost] = {
    withSQL {
      select.from(VariableCost as vc).where.append(where)
    }.map(VariableCost(vc.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(VariableCost as vc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      userId: Int,
      categoryId: Int,
      price: Int,
      opinion: Option[String] = None,
      paymentAt: DateTime,
      address: Option[String] = None,
      latitude: Option[BigDecimal] = None,
      longitude: Option[BigDecimal] = None,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): VariableCost = {
    val generatedKey = withSQL {
      insert
        .into(VariableCost)
        .namedValues(
          column.userId -> userId,
          column.categoryId -> categoryId,
          column.price -> price,
          column.opinion -> opinion,
          column.paymentAt -> paymentAt,
          column.address -> address,
          column.latitude -> latitude,
          column.longitude -> longitude,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    VariableCost(
      id = generatedKey.toInt,
      userId = userId,
      categoryId = categoryId,
      price = price,
      opinion = opinion,
      paymentAt = paymentAt,
      address = address,
      latitude = latitude,
      longitude = longitude,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[VariableCost]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "userId" -> entity.userId,
        "categoryId" -> entity.categoryId,
        "price" -> entity.price,
        "opinion" -> entity.opinion,
        "paymentAt" -> entity.paymentAt,
        "address" -> entity.address,
        "latitude" -> entity.latitude,
        "longitude" -> entity.longitude,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into variable_costs(
      user_id,
      category_id,
      price,
      opinion,
      payment_at,
      address,
      latitude,
      longitude,
      created_at,
      updated_at
    ) values (
      {userId},
      {categoryId},
      {price},
      {opinion},
      {paymentAt},
      {address},
      {latitude},
      {longitude},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(
      entity: VariableCost
  )(implicit session: DBSession = autoSession): VariableCost = {
    withSQL {
      update(VariableCost)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.categoryId -> entity.categoryId,
          column.price -> entity.price,
          column.opinion -> entity.opinion,
          column.paymentAt -> entity.paymentAt,
          column.address -> entity.address,
          column.latitude -> entity.latitude,
          column.longitude -> entity.longitude,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(
      entity: VariableCost
  )(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(VariableCost).where.eq(column.id, entity.id) }.update
      .apply()
  }

}
