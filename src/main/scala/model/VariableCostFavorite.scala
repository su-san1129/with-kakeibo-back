package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._

case class VariableCostFavorite(
    id: Int,
    userId: Int,
    variableCostId: Int,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit
      session: DBSession = VariableCostFavorite.autoSession
  ): VariableCostFavorite = VariableCostFavorite.save(this)(session)

  def destroy()(implicit
      session: DBSession = VariableCostFavorite.autoSession
  ): Int = VariableCostFavorite.destroy(this)(session)

}

object VariableCostFavorite extends SQLSyntaxSupport[VariableCostFavorite] {

  override val tableName = "variable_cost_favorites"

  override val columns =
    Seq("id", "user_id", "variable_cost_id", "created_at", "updated_at")

  def apply(vcf: SyntaxProvider[VariableCostFavorite])(
      rs: WrappedResultSet
  ): VariableCostFavorite = apply(vcf.resultName)(rs)
  def apply(
      vcf: ResultName[VariableCostFavorite]
  )(rs: WrappedResultSet): VariableCostFavorite =
    new VariableCostFavorite(
      id = rs.get(vcf.id),
      userId = rs.get(vcf.userId),
      variableCostId = rs.get(vcf.variableCostId),
      createdAt = rs.get(vcf.createdAt),
      updatedAt = rs.get(vcf.updatedAt)
    )

  val vcf = VariableCostFavorite.syntax("vcf")

  override val autoSession = AutoSession

  def find(
      id: Int
  )(implicit session: DBSession = autoSession): Option[VariableCostFavorite] = {
    withSQL {
      select.from(VariableCostFavorite as vcf).where.eq(vcf.id, id)
    }.map(VariableCostFavorite(vcf.resultName)).single.apply()
  }

  def findAll()(implicit
      session: DBSession = autoSession
  ): List[VariableCostFavorite] = {
    withSQL(select.from(VariableCostFavorite as vcf))
      .map(VariableCostFavorite(vcf.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(VariableCostFavorite as vcf))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[VariableCostFavorite] = {
    withSQL {
      select.from(VariableCostFavorite as vcf).where.append(where)
    }.map(VariableCostFavorite(vcf.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[VariableCostFavorite] = {
    withSQL {
      select.from(VariableCostFavorite as vcf).where.append(where)
    }.map(VariableCostFavorite(vcf.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(VariableCostFavorite as vcf).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      userId: Int,
      variableCostId: Int,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): VariableCostFavorite = {
    val generatedKey = withSQL {
      insert
        .into(VariableCostFavorite)
        .namedValues(
          column.userId -> userId,
          column.variableCostId -> variableCostId,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    VariableCostFavorite(
      id = generatedKey.toInt,
      userId = userId,
      variableCostId = variableCostId,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[VariableCostFavorite]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "userId" -> entity.userId,
        "variableCostId" -> entity.variableCostId,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into variable_cost_favorites(
      user_id,
      variable_cost_id,
      created_at,
      updated_at
    ) values (
      {userId},
      {variableCostId},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(
      entity: VariableCostFavorite
  )(implicit session: DBSession = autoSession): VariableCostFavorite = {
    withSQL {
      update(VariableCostFavorite)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.variableCostId -> entity.variableCostId,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(
      entity: VariableCostFavorite
  )(implicit session: DBSession = autoSession): Int = {
    withSQL {
      delete.from(VariableCostFavorite).where.eq(column.id, entity.id)
    }.update.apply()
  }

}
