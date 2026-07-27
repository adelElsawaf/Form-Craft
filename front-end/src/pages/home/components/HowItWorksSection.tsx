import { Box, Container, Grid, Typography } from '@mui/material'
import DesignServicesOutlinedIcon from '@mui/icons-material/DesignServicesOutlined'
import PublishOutlinedIcon from '@mui/icons-material/PublishOutlined'
import InboxOutlinedIcon from '@mui/icons-material/InboxOutlined'
import InsightsOutlinedIcon from '@mui/icons-material/InsightsOutlined'
import { SectionHeader } from '@/pages/home/components/SectionHeader'
import { HOME_SECTIONS } from '@/shared/lib/scrollToSection'

const steps = [
  {
    icon: DesignServicesOutlinedIcon,
    title: 'Design',
    description: 'Drag fields, set validation, and shape the layout visually.',
  },
  {
    icon: PublishOutlinedIcon,
    title: 'Publish',
    description: 'Share a link or embed the form anywhere in one click.',
  },
  {
    icon: InboxOutlinedIcon,
    title: 'Collect',
    description: 'Responses land in your dashboard the moment they arrive.',
  },
  {
    icon: InsightsOutlinedIcon,
    title: 'Review',
    description: 'Filter submissions, export data, and track completion.',
  },
]

export function HowItWorksSection() {
  return (
    <Box
      id={HOME_SECTIONS.howItWorks}
      component="section"
      sx={{
        py: { xs: 7, md: 9 },
        scrollMarginTop: { xs: 80, md: 88 },
        bgcolor: 'background.paper',
        borderTop: '1px solid',
        borderColor: 'divider',
      }}
    >
      <Container maxWidth="lg">
        <SectionHeader
          overline="How it works"
          title="From design to live form in four steps"
          description="Every step is visible upfront — no hidden flow."
          align="center"
        />

        <Grid container spacing={3}>
          {steps.map((step, index) => {
            const Icon = step.icon

            return (
              <Grid key={step.title} size={{ xs: 12, sm: 6, md: 3 }}>
                <Box sx={{ position: 'relative', height: '100%' }}>
                  {index < steps.length - 1 && (
                    <Box
                      sx={{
                        display: { xs: 'none', md: 'block' },
                        position: 'absolute',
                        top: 28,
                        left: 'calc(50% + 28px)',
                        width: 'calc(100% - 56px)',
                        height: 2,
                        bgcolor: 'divider',
                        zIndex: 0,
                      }}
                    />
                  )}

                  <Box sx={{ textAlign: 'center', position: 'relative', zIndex: 1 }}>
                    <Box
                      sx={{
                        width: 56,
                        height: 56,
                        borderRadius: '50%',
                        mx: 'auto',
                        mb: 1.5,
                        display: 'grid',
                        placeItems: 'center',
                        bgcolor: 'primary.main',
                        color: 'primary.contrastText',
                        boxShadow: '0 8px 20px rgba(79, 70, 229, 0.25)',
                      }}
                    >
                      <Icon fontSize="small" />
                    </Box>

                    <Typography variant="caption" color="text.secondary" sx={{ fontWeight: 700 }}>
                      Step {index + 1}
                    </Typography>
                    <Typography variant="subtitle1" sx={{ fontWeight: 800, mb: 0.75, mt: 0.25 }}>
                      {step.title}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ lineHeight: 1.65 }}>
                      {step.description}
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
