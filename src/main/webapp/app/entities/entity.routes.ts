import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'distribuidoraApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'distribuidora',
    data: { pageTitle: 'distribuidoraApp.distribuidora.home.title' },
    loadChildren: () => import('./distribuidora/distribuidora.routes'),
  },
  {
    path: 'produto',
    data: { pageTitle: 'distribuidoraApp.produto.home.title' },
    loadChildren: () => import('./produto/produto.routes'),
  },
  {
    path: 'tipo',
    data: { pageTitle: 'distribuidoraApp.tipo.home.title' },
    loadChildren: () => import('./tipo/tipo.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
