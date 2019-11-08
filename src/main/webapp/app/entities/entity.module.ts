import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'alien',
        loadChildren: () => import('./alien/alien.module').then(m => m.ReBaseAlienModule)
      },
      {
        path: 'world',
        loadChildren: () => import('./world/world.module').then(m => m.ReBaseWorldModule)
      },
      {
        path: 'solar-system',
        loadChildren: () => import('./solar-system/solar-system.module').then(m => m.ReBaseSolarSystemModule)
      },
      {
        path: 'classification',
        loadChildren: () => import('./classification/classification.module').then(m => m.ReBaseClassificationModule)
      },
      {
        path: 'technology',
        loadChildren: () => import('./technology/technology.module').then(m => m.ReBaseTechnologyModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class ReBaseEntityModule {}
