package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._

case class UserRelationship(
    id: Int,
    followingId: Int,
    followedId: Int,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit
      session: DBSession = UserRelationship.autoSession
  ): UserRelationship = UserRelationship.save(this)(session)

  def destroy()(implicit
      session: DBSession = UserRelationship.autoSession
  ): Int = UserRelationship.destroy(this)(session)

}

object UserRelationship extends SQLSyntaxSupport[UserRelationship] {

  override val tableName = "user_relationships"

  override val columns =
    Seq("id", "following_id", "followed_id", "created_at", "updated_at")

  def apply(ur: SyntaxProvider[UserRelationship])(
      rs: WrappedResultSet
  ): UserRelationship = apply(ur.resultName)(rs)
  def apply(
      ur: ResultName[UserRelationship]
  )(rs: WrappedResultSet): UserRelationship =
    new UserRelationship(
      id = rs.get(ur.id),
      followingId = rs.get(ur.followingId),
      followedId = rs.get(ur.followedId),
      createdAt = rs.get(ur.createdAt),
      updatedAt = rs.get(ur.updatedAt)
    )

  val ur = UserRelationship.syntax("ur")

  override val autoSession = AutoSession

  def find(
      id: Int
  )(implicit session: DBSession = autoSession): Option[UserRelationship] = {
    withSQL {
      select.from(UserRelationship as ur).where.eq(ur.id, id)
    }.map(UserRelationship(ur.resultName)).single.apply()
  }

  def findAll()(implicit
      session: DBSession = autoSession
  ): List[UserRelationship] = {
    withSQL(select.from(UserRelationship as ur))
      .map(UserRelationship(ur.resultName))
      .list
      .apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(UserRelationship as ur))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[UserRelationship] = {
    withSQL {
      select.from(UserRelationship as ur).where.append(where)
    }.map(UserRelationship(ur.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[UserRelationship] = {
    withSQL {
      select.from(UserRelationship as ur).where.append(where)
    }.map(UserRelationship(ur.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(UserRelationship as ur).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      followingId: Int,
      followedId: Int,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): UserRelationship = {
    val generatedKey = withSQL {
      insert
        .into(UserRelationship)
        .namedValues(
          column.followingId -> followingId,
          column.followedId -> followedId,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    UserRelationship(
      id = generatedKey.toInt,
      followingId = followingId,
      followedId = followedId,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[UserRelationship]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "followingId" -> entity.followingId,
        "followedId" -> entity.followedId,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into user_relationships(
      following_id,
      followed_id,
      created_at,
      updated_at
    ) values (
      {followingId},
      {followedId},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(
      entity: UserRelationship
  )(implicit session: DBSession = autoSession): UserRelationship = {
    withSQL {
      update(UserRelationship)
        .set(
          column.id -> entity.id,
          column.followingId -> entity.followingId,
          column.followedId -> entity.followedId,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(
      entity: UserRelationship
  )(implicit session: DBSession = autoSession): Int = {
    withSQL {
      delete.from(UserRelationship).where.eq(column.id, entity.id)
    }.update.apply()
  }

}
