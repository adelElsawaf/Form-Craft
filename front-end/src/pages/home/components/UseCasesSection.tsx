import { Box, Container, Grid, Typography } from '@mui/material'
import BusinessCenterOutlinedIcon from '@mui/icons-material/BusinessCenterOutlined'
import CampaignOutlinedIcon from '@mui/icons-material/CampaignOutlined'
import SchoolOutlinedIcon from '@mui/icons-material/SchoolOutlined'
import SupportAgentOutlinedIcon from '@mui/icons-material/SupportAgentOutlined'
import { SectionHeader } from '@/pages/home/components/SectionHeader'
import { HOME_SECTIONS } from '@/shared/lib/scrollToSection'

const useCases = [
  {
    icon: BusinessCenterOutlinedIcon,
    accent: '#4F46E5',
    title: 'Client intake',
    description: 'Capture briefs, budgets, and timelines in one polished flow.',
  },
  {
    icon: CampaignOutlinedIcon,
    accent: '#06B6D4',
    title: 'Lead generation',
    description: 'Turn landing pages into high-converting signup forms.',
  },
  {
    icon: SchoolOutlinedIcon,
    accent: '#4338CA',
    title: 'Applications',
    description: 'Run course sign-ups, quizzes, and structured surveys.',
  },
  {
    icon: SupportAgentOutlinedIcon,
    accent: '#0891B2',
    title: 'Contact forms',
    description: 'Replace messy email threads with organized requests.',
  },
]

export function UseCasesSection() {
  return (
    <Box
      id={HOME_SECTIONS.useCases}
      component="section"
      sx={{
        py: { xs: 7, md: 9 },
        scrollMarginTop: { xs: 80, md: 88 },
        bgcolor: 'background.default',
      }}
    >
      <Container maxWidth="lg">
        <SectionHeader
          overline="Use cases"
          title="Popular ways people use FormCraft"
          description="All scenarios listed below — pick what matches your workflow."
          align="center"
        />

        <Grid container spacing={2}>
          {useCases.map((useCase) => {
            const Icon = useCase.icon

            return (
              <Grid key={useCase.title} size={{ xs: 12, sm: 6 }}>
                <Box
                  sx={{
                    display: 'flex',
                    gap: 2,
                    p: 2.5,
                    borderRadius: 3,
                    bgcolor: 'background.paper',
                    border: '1px solid',
                    borderColor: 'divider',
                    borderLeft: `4px solid ${useCase.accent}`,
                    height: '100%',
                    transition: 'box-shadow 0.2s ease',
                    '&:hover': {
                      boxShadow: '0 8px 24px rgba(15, 23, 42, 0.06)',
                    },
                  }}
                >
                  <Box
                    sx={{
                      width: 40,
                      height: 40,
                      borderRadius: 2,
                      flexShrink: 0,
                      display: 'grid',
                      placeItems: 'center',
                      bgcolor: `${useCase.accent}14`,
                      color: useCase.accent,
                    }}
                  >
                    <Icon fontSize="small" />
                  </Box>

                  <Box>
                    <Typography variant="subtitle2" sx={{ fontWeight: 800, mb: 0.5 }}>
                      {useCase.title}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ lineHeight: 1.6 }}>
                      {useCase.description}
                    </Typography>
                  </Box>
                </Box>
              </Grid>
            )
          })}
        </Grid>
      </Container>
    </Box>
  )
}
