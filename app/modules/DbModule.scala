package modules

import com.google.inject.AbstractModule
import infrastructure.persistence.repositories.{ExpenseRepository, ExpenseRepositoryImpl}

class DbModule extends AbstractModule {

  override def configure() = {
    bind(classOf[ExpenseRepository])
      .to(classOf[ExpenseRepositoryImpl])
  }
}
