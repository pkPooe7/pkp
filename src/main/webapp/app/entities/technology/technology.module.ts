import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReBaseSharedModule } from 'app/shared/shared.module';
import { TechnologyComponent } from './technology.component';
import { TechnologyDetailComponent } from './technology-detail.component';
import { TechnologyUpdateComponent } from './technology-update.component';
import { TechnologyDeletePopupComponent, TechnologyDeleteDialogComponent } from './technology-delete-dialog.component';
import { technologyRoute, technologyPopupRoute } from './technology.route';

const ENTITY_STATES = [...technologyRoute, ...technologyPopupRoute];

@NgModule({
  imports: [ReBaseSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TechnologyComponent,
    TechnologyDetailComponent,
    TechnologyUpdateComponent,
    TechnologyDeleteDialogComponent,
    TechnologyDeletePopupComponent
  ],
  entryComponents: [TechnologyDeleteDialogComponent]
})
export class ReBaseTechnologyModule {}
