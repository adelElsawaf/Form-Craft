import { AuthSplitLayout } from '@/features/auth/components/AuthSplitLayout'
import { RegisterForm } from '@/features/auth/components/RegisterForm'
import { useDocumentTitle } from '@/shared/hooks/useDocumentTitle'
import { FREE_FORM_LIMIT } from '@/shared/lib/scrollToSection'

const perks = [
  `${FREE_FORM_LIMIT} forms free — no card required`,
  'Drag-and-drop form builder',
  'Publish and share in minutes',
  'Collect responses in one place',
]

export default function RegisterPage() {
  useDocumentTitle('Register | FormCraft')

  return (
    <AuthSplitLayout
      brandTitle="Start building forms for free"
      brandSubtitle="Create your account and launch your first form in minutes — no credit card needed."
      perks={perks}
      mobileBadge={`${FREE_FORM_LIMIT} forms free — no card required`}
      title="Create your account"
      subtitle="Join FormCraft and start building in minutes."
    >
      <RegisterForm />
    </AuthSplitLayout>
  )
}
