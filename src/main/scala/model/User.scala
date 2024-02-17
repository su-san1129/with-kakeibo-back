package work.withkakeibo
package model

import scalikejdbc._
import org.joda.time.DateTime
import scalikejdbc.jodatime.JodaParameterBinderFactory._
import scalikejdbc.jodatime.JodaTypeBinder._
import java.sql.{Blob}

case class User(
    id: Int,
    name: String,
    email: String,
    password: String,
    introduction: Option[String] = None,
    profileImage: Option[Blob] = None,
    currentSignInAt: Option[DateTime] = None,
    lastSignInAt: Option[DateTime] = None,
    createdAt: Option[DateTime] = None,
    updatedAt: Option[DateTime] = None
) {

  def save()(implicit session: DBSession = User.autoSession): User =
    User.save(this)(session)

  def destroy()(implicit session: DBSession = User.autoSession): Int =
    User.destroy(this)(session)

}

object User extends SQLSyntaxSupport[User] {

  override val tableName = "users"

  override val columns = Seq(
    "id",
    "name",
    "email",
    "password",
    "introduction",
    "profile_image",
    "current_sign_in_at",
    "last_sign_in_at",
    "created_at",
    "updated_at"
  )

  def apply(u: SyntaxProvider[User])(rs: WrappedResultSet): User =
    apply(u.resultName)(rs)
  def apply(u: ResultName[User])(rs: WrappedResultSet): User =
    new User(
      id = rs.get(u.id),
      name = rs.get(u.name),
      email = rs.get(u.email),
      password = rs.get(u.password),
      introduction = rs.get(u.introduction),
      profileImage = rs.get(u.profileImage),
      currentSignInAt = rs.get(u.currentSignInAt),
      lastSignInAt = rs.get(u.lastSignInAt),
      createdAt = rs.get(u.createdAt),
      updatedAt = rs.get(u.updatedAt)
    )

  val u = User.syntax("u")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.eq(u.id, id)
    }.map(User(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[User] = {
    withSQL(select.from(User as u)).map(User(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(User as u))
      .map(rs => rs.long(1))
      .single
      .apply()
      .get
  }

  def findBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Option[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).single.apply()
  }

  def findAllBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): List[User] = {
    withSQL {
      select.from(User as u).where.append(where)
    }.map(User(u.resultName)).list.apply()
  }

  def countBy(
      where: SQLSyntax
  )(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(User as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
      name: String,
      email: String,
      password: String,
      introduction: Option[String] = None,
      profileImage: Option[Blob] = None,
      currentSignInAt: Option[DateTime] = None,
      lastSignInAt: Option[DateTime] = None,
      createdAt: Option[DateTime] = None,
      updatedAt: Option[DateTime] = None
  )(implicit session: DBSession = autoSession): User = {
    val generatedKey = withSQL {
      insert
        .into(User)
        .namedValues(
          column.name -> name,
          column.email -> email,
          column.password -> password,
          column.introduction -> introduction,
          column.profileImage -> profileImage,
          column.currentSignInAt -> currentSignInAt,
          column.lastSignInAt -> lastSignInAt,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt
        )
    }.updateAndReturnGeneratedKey.apply()

    User(
      id = generatedKey.toInt,
      name = name,
      email = email,
      password = password,
      introduction = introduction,
      profileImage = profileImage,
      currentSignInAt = currentSignInAt,
      lastSignInAt = lastSignInAt,
      createdAt = createdAt,
      updatedAt = updatedAt
    )
  }

  def batchInsert(
      entities: collection.Seq[User]
  )(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(String, Any)]] = entities.map(entity =>
      Seq(
        "name" -> entity.name,
        "email" -> entity.email,
        "password" -> entity.password,
        "introduction" -> entity.introduction,
        "profileImage" -> entity.profileImage,
        "currentSignInAt" -> entity.currentSignInAt,
        "lastSignInAt" -> entity.lastSignInAt,
        "createdAt" -> entity.createdAt,
        "updatedAt" -> entity.updatedAt
      )
    )
    SQL("""insert into users(
      name,
      email,
      password,
      introduction,
      profile_image,
      current_sign_in_at,
      last_sign_in_at,
      created_at,
      updated_at
    ) values (
      {name},
      {email},
      {password},
      {introduction},
      {profileImage},
      {currentSignInAt},
      {lastSignInAt},
      {createdAt},
      {updatedAt}
    )""").batchByName(params.toSeq: _*).apply[List]()
  }

  def save(entity: User)(implicit session: DBSession = autoSession): User = {
    withSQL {
      update(User)
        .set(
          column.id -> entity.id,
          column.name -> entity.name,
          column.email -> entity.email,
          column.password -> entity.password,
          column.introduction -> entity.introduction,
          column.profileImage -> entity.profileImage,
          column.currentSignInAt -> entity.currentSignInAt,
          column.lastSignInAt -> entity.lastSignInAt,
          column.createdAt -> entity.createdAt,
          column.updatedAt -> entity.updatedAt
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: User)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(User).where.eq(column.id, entity.id) }.update.apply()
  }

}
