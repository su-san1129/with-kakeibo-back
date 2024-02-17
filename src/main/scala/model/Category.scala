package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._

case class Category(
    id: Int,
    userId: Int,
    name: String,
    status: String,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit session: DBSession = Category.autoSession): Category =
    Category.save(this)(session)

  def destroy()(implicit session: DBSession = Category.autoSession): Int =
    Category.destroy(this)(session)

}

object Category extends SQLSyntaxSupport[Category] {

  override val tableName = "categories"

  override val columns =
    Seq("id", "user_id", "name", "status", "created_at", "updated_at")

  def apply(c: SyntaxProvider[Category])(rs: WrappedResultSet): Category =
    apply(c.resultName)(rs)
  def apply(c: ResultName[Category])(rs: WrappedResultSet): Category =
    new Category(
      id = rs.get(c.id),
      userId = rs.get(c.userId),
      name = rs.get(c.name),
      status = rs.get(c.status),
      createdAt = rs.get(c.createdAt),
      updatedAt = rs.get(c.updatedAt)
    )

  val c = Category.syntax("c")

  override val autoSession = AutoSession

  def find(
      id: Int
  )(implicit session: DBSession = autoSession): Option[Category] = {
    withSQL {
      select.from(Category as c).where.eq(c.id, id)
    }.map(Category(c.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Category] = {
    withSQL(select.from(Category as c)).map(Category(c.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Category as c))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[Category] = {
    withSQL {
      select.from(Category as c).where.append(where)
    }.map(Category(c.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[Category] = {
    withSQL {
      select.from(Category as c).where.append(where)
    }.map(Category(c.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Category as c).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      userId: Int,
      name: String,
      status: String,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): Category = {
    val generatedKey = withSQL {
      insert
        .into(Category)
        .namedValues(
          column.userId -> userId,
          column.name -> name,
          column.status -> status,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    Category(
      id = generatedKey.toInt,
      userId = userId,
      name = name,
      status = status,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[Category]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "userId" -> entity.userId,
        "name" -> entity.name,
        "status" -> entity.status,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into categories(
      user_id,
      name,
      status,
      created_at,
      updated_at
    ) values (
      {userId},
      {name},
      {status},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(
      entity: Category
  )(implicit session: DBSession = autoSession): Category = {
    withSQL {
      update(Category)
        .set(
          column.id -> entity.id,
          column.userId -> entity.userId,
          column.name -> entity.name,
          column.status -> entity.status,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(
      entity: Category
  )(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Category).where.eq(column.id, entity.id) }.update
      .apply()
  }

}
