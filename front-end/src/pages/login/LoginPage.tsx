import { AuthSplitLayout } from '@/features/auth/components/AuthSplitLayout'
import { LoginForm } from '@/features/auth/components/LoginForm'
import { useDocumentTitle } from '@/shared/hooks/useDocumentTitle'

const perks = [
  'Access your dashboard instantly',
  'Manage forms and submissions',
  'Track responses in real time',
  'Pick up right where you left off',
]

export default function LoginPage() {
  useDocumentTitle('Sign in | FormCraft')

  return (
    <AuthSplitLayout
      reversed
      brandTitle="Welcome back"
      brandSubtitle="Sign in to manage your forms, review submissions, and keep building."
      perks={perks}
      title="Sign in to your account"
      subtitle="Enter your credentials to continue to your dashboard."
    >
      <LoginForm />
    </AuthSplitLayout>
  )
}
