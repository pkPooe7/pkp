import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { ReBaseSharedModule } from 'app/shared/shared.module';
import { ReBaseCoreModule } from 'app/core/core.module';
import { ReBaseAppRoutingModule } from './app-routing.module';
import { ReBaseHomeModule } from './home/home.module';
import { ReBaseEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { JhiMainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    ReBaseSharedModule,
    ReBaseCoreModule,
    ReBaseHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    ReBaseEntityModule,
    ReBaseAppRoutingModule
  ],
  declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [JhiMainComponent]
})
export class ReBaseAppModule {}
