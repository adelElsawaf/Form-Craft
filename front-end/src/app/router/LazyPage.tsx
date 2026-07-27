import { Suspense, type ComponentType, type LazyExoticComponent } from 'react'
import { LoadingScreen } from '@/shared/components/feedback/LoadingScreen'

type LazyPageProps = {
  page: LazyExoticComponent<ComponentType>
}

export function LazyPage({ page: Page }: LazyPageProps) {
  return (
    <Suspense fallback={<LoadingScreen />}>
      <Page />
    </Suspense>
  )
}
