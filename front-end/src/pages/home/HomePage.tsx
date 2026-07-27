import { HeroSection } from '@/pages/home/components/HeroSection'
import { HowItWorksSection } from '@/pages/home/components/HowItWorksSection'
import { PricingSection } from '@/pages/home/components/PricingSection'
import { UseCasesSection } from '@/pages/home/components/UseCasesSection'
import { useDocumentTitle } from '@/shared/hooks/useDocumentTitle'
import { useScrollToHash } from '@/shared/hooks/useScrollToHash'

export default function HomePage() {
  useDocumentTitle('FormCraft — Build forms that feel effortless')
  useScrollToHash()

  return (
    <>
      <HeroSection />
      <HowItWorksSection />
      <UseCasesSection />
      <PricingSection />
    </>
  )
}
