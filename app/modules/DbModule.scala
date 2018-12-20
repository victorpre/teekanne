package modules

import com.google.inject.AbstractModule
import infrastructure.persistence.repositories.{BillRepository, BillRepositoryImpl}

class DbModule extends AbstractModule {

  override def configure() = {
    bind(classOf[BillRepository])
      .to(classOf[BillRepositoryImpl])
  }
}
