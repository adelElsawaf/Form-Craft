export const HOME_SECTIONS = {
  howItWorks: 'how-it-works',
  useCases: 'use-cases',
  pricing: 'pricing',
} as const

export type HomeSectionId = (typeof HOME_SECTIONS)[keyof typeof HOME_SECTIONS]

export function homeSectionHash(sectionId: HomeSectionId) {
  return `#${sectionId}`
}

export function scrollToSection(sectionId: string) {
  const element = document.getElementById(sectionId)

  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

export const FREE_FORM_LIMIT = 5
