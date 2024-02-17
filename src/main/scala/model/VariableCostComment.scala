package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._

case class VariableCostComment(
    id: Int,
    userId: Int,
    variableCostId: Int,
    comment: String,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit
      session: DBSession = VariableCostComment.autoSession
  ): VariableCostComment = VariableCostComment.save(this)(session)

  def destroy()(implicit
      session: DBSession = VariableCostComment.autoSession
  ): Int = VariableCostComment.destroy(this)(session)

}

object VariableCostComment extends SQLSyntaxSupport[VariableCostComment] {

  override val tableName = "variable_cost_comments"

  override val columns = Seq(
    "id",
    "user_id",
    "variable_cost_id",
    "comment",
    "created_at",
    "updated_at"
  )

  def apply(vcc: SyntaxProvider[VariableCostComment])(
      rs: WrappedResultSet
  ): VariableCostComment = apply(vcc.resultName)(rs)
  def apply(
      vcc: ResultName[VariableCostComment]
  )(rs: WrappedResultSet): VariableCostComment =
    new VariableCostComment(
      id = rs.get(vcc.id),
      userId = rs.get(vcc.userId),
      variableCostId = rs.get(vcc.variableCostId),
      comment = rs.get(vcc.comment),
      createdAt = rs.get(vcc.createdAt),
      updatedAt = rs.get(vcc.updatedAt)
    )

  val vcc = VariableCostComment.syntax("vcc")

  override val autoSession = AutoSession

  def find(
      id: Int
  )(implicit session: DBSession = autoSession): Option[VariableCostComment] = {
    withSQL {
      select.from(VariableCostComment as vcc).where.eq(vcc.id, id)
    }.map(VariableCostComment(vcc.resultName)).single.apply()
  }

  def findAll()(implicit
      session: DBSession = autoSession
  ): List[VariableCostComment] = {
    withSQL(select.from(VariableCostComment as vcc))
      .map(VariableCostComment(vcc.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(VariableCostComment as vcc))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[VariableCostComment] = {
    withSQL {
      select.from(VariableCostComment as vcc).where.append(where)
    }.map(VariableCostComment(vcc.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[VariableCostComment] = {
    withSQL {
      select.from(VariableCostComment as vcc).where.append(where)
    }.map(VariableCostComment(vcc.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(VariableCostComment as vcc).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      userId: Int,
      variableCostId: Int,
      comment: String,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): VariableCostComment = {
    val generatedKey = withSQL {
      insert
        .into(VariableCostComment)
        .namedValues(
          column.userId -> userId,
          column.variableCostId -> variableCostId,
          column.comment -> comment,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    VariableCostComment(
      id = generatedKey.toInt,
      userId = userId,
      variableCostId = variableCostId,
      comment = comment,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[VariableCostComment]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "userId" -> entity.userId,
        "variableCostId" -> entity.variableCostId,
        "comment" -> entity.comment,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into variable_cost_comments(
      user_id,
      variable_cost_id,
      comment,
      created_at,
      updated_at
    ) values (
      {userId},
      {variableCostId},
      {comment},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(
      entity: VariableCostComment
  )(implicit session: DBSession = autoSession): VariableCostComment = {
    withSQL {
      update(VariableCostComment)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.variableCostId -> entity.variableCostId,
          column.comment -> entity.comment,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(
      entity: VariableCostComment
  )(implicit session: DBSession = autoSession): Int = {
    withSQL {
      delete.from(VariableCostComment).where.eq(column.id, entity.id)
    }.update.apply()
  }

}
