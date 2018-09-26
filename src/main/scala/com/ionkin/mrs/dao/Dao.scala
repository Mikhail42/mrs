package com.ionkin.mrs.dao

import com.ionkin.mrs.model.Data.context._

private[dao] trait Dao {
  private[dao] def quoted[T](fil: T => Boolean)(implicit scheme: SchemaMeta[T]): Quoted[Query[T]] = quote {
    query[T].filter(t => fil(t))
  }
  private[dao] def optMax[T <: Ordered[T]](list: List[T]): Option[T] = if (list.isEmpty) None else Some(list.max)
}
