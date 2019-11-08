import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ReBaseSharedModule } from 'app/shared/shared.module';
import { WorldComponent } from './world.component';
import { WorldDetailComponent } from './world-detail.component';
import { WorldUpdateComponent } from './world-update.component';
import { WorldDeletePopupComponent, WorldDeleteDialogComponent } from './world-delete-dialog.component';
import { worldRoute, worldPopupRoute } from './world.route';

const ENTITY_STATES = [...worldRoute, ...worldPopupRoute];

@NgModule({
  imports: [ReBaseSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [WorldComponent, WorldDetailComponent, WorldUpdateComponent, WorldDeleteDialogComponent, WorldDeletePopupComponent],
  entryComponents: [WorldDeleteDialogComponent]
})
export class ReBaseWorldModule {}
