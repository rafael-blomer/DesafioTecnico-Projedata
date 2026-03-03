import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import RawMaterialsView from '../views/RawMaterialsView.vue'
import ProductsView from '../views/ProductsView.vue'
import OptimizerView from '../views/OptimizerView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: HomeView },
    { path: '/raw-materials', component: RawMaterialsView },
    { path: '/products', component: ProductsView },
    { path: '/optimizer', component: OptimizerView }
  ]
})

export default router
