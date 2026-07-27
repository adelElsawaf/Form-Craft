import AddIcon from '@mui/icons-material/Add'
import DynamicFormOutlinedIcon from '@mui/icons-material/DynamicFormOutlined'
import InboxOutlinedIcon from '@mui/icons-material/InboxOutlined'
import TrendingUpOutlinedIcon from '@mui/icons-material/TrendingUpOutlined'
import { Box, Button, Grid, Stack, Typography } from '@mui/material'
import { useAuth } from '@/features/auth/hooks/useAuth'
import { FREE_FORM_LIMIT } from '@/shared/lib/scrollToSection'
import { useDocumentTitle } from '@/shared/hooks/useDocumentTitle'
import { formatFullName } from '@/shared/lib/formatters'

const summaryCards = [
  {
    label: 'Forms',
    value: '0',
    hint: `${FREE_FORM_LIMIT} free remaining`,
    icon: DynamicFormOutlinedIcon,
    accent: '#4F46E5',
  },
  {
    label: 'Submissions',
    value: '0',
    hint: 'No responses yet',
    icon: InboxOutlinedIcon,
    accent: '#06B6D4',
  },
  {
    label: 'Completion',
    value: '—',
    hint: 'Appears after first form',
    icon: TrendingUpOutlinedIcon,
    accent: '#4338CA',
  },
]

export default function DashboardPage() {
  useDocumentTitle('Dashboard | FormCraft')
  const { user } = useAuth()
  const firstName = user?.firstName ?? 'there'

  return (
    <Stack spacing={4}>
      <Stack
        direction={{ xs: 'column', sm: 'row' }}
        spacing={2}
        sx={{
          alignItems: { xs: 'stretch', sm: 'flex-end' },
          justifyContent: 'space-between',
        }}
      >
        <Box>
          <Typography
            variant="h4"
            sx={{
              fontWeight: 800,
              letterSpacing: '-0.03em',
              fontSize: { xs: '1.75rem', md: '2rem' },
              mb: 0.75,
            }}
          >
            Welcome back, {firstName}
          </Typography>
          <Typography color="text.secondary" sx={{ maxWidth: 480, lineHeight: 1.6 }}>
            {user
              ? `${formatFullName(user.firstName, user.lastName)} — manage your forms and responses from here.`
              : 'Manage your forms and responses from here.'}
          </Typography>
        </Box>

        <Button
          variant="contained"
          size="large"
          startIcon={<AddIcon />}
          disabled
          sx={{ alignSelf: { xs: 'stretch', sm: 'flex-end' }, whiteSpace: 'nowrap' }}
        >
          Create form
        </Button>
      </Stack>

      <Grid container spacing={2.5}>
        {summaryCards.map((card) => {
          const Icon = card.icon

          return (
            <Grid key={card.label} size={{ xs: 12, sm: 4 }}>
              <Box
                sx={{
                  p: 2.5,
                  height: '100%',
                  borderRadius: 3,
                  bgcolor: 'background.paper',
                  border: '1px solid',
                  borderColor: 'divider',
                  borderLeft: `4px solid ${card.accent}`,
                }}
              >
                <Stack direction="row" spacing={1.5} sx={{ alignItems: 'flex-start', mb: 2 }}>
                  <Box
                    sx={{
                      width: 40,
                      height: 40,
                      borderRadius: 2,
                      display: 'grid',
                      placeItems: 'center',
                      bgcolor: `${card.accent}14`,
                      color: card.accent,
                      flexShrink: 0,
                    }}
                  >
                    <Icon fontSize="small" />
                  </Box>
                  <Typography variant="body2" color="text.secondary" sx={{ fontWeight: 600, pt: 0.75 }}>
                    {card.label}
                  </Typography>
                </Stack>
                <Typography variant="h4" sx={{ fontWeight: 800, letterSpacing: '-0.03em', mb: 0.5 }}>
                  {card.value}
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  {card.hint}
                </Typography>
              </Box>
            </Grid>
          )
        })}
      </Grid>

      <Box
        sx={{
          p: { xs: 3, md: 5 },
          borderRadius: 4,
          bgcolor: 'background.paper',
          border: '1px solid',
          borderColor: 'divider',
          textAlign: 'center',
        }}
      >
        <Box
          sx={{
            width: 64,
            height: 64,
            borderRadius: 3,
            mx: 'auto',
            mb: 2.5,
            display: 'grid',
            placeItems: 'center',
            bgcolor: 'rgba(79, 70, 229, 0.08)',
            color: 'primary.main',
          }}
        >
          <DynamicFormOutlinedIcon sx={{ fontSize: 32 }} />
        </Box>

        <Typography variant="h5" sx={{ fontWeight: 800, letterSpacing: '-0.02em', mb: 1 }}>
          No forms yet
        </Typography>
        <Typography
          color="text.secondary"
          sx={{ maxWidth: 420, mx: 'auto', mb: 3, lineHeight: 1.7 }}
        >
          Create your first form to start collecting responses. You get {FREE_FORM_LIMIT} forms free —
          no card required.
        </Typography>

        <Button variant="contained" size="large" startIcon={<AddIcon />} disabled>
          Create your first form
        </Button>
      </Box>
    </Stack>
  )
}
