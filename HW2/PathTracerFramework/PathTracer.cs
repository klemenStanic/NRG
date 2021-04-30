using System;
using static PathTracer.Samplers;

namespace PathTracer
{
    class PathTracer
    {
        public Spectrum Li(Ray r, Scene s)
        {
            // Initialize L and beta
            Spectrum L = Spectrum.ZeroSpectrum;
            Spectrum beta = Spectrum.Create(1.0);


            int max_n_bounces = 20;
            int nbounces = 0;
            while (nbounces < max_n_bounces)
            {
                // Calculate intersection with the scene
                (double? distance, SurfaceInteraction intersection) = s.Intersect(r);


                // We may not hit anyting, return 0
                if (!distance.HasValue) { return L; }
                Object intersectionObj = intersection.Obj;

                Vector3 wo = -r.d;

                // Check if we hit a light
                if (intersectionObj is Light)
                {
                    // L <- B * Le(wo)
                    // Path reuse
                    if (nbounces == 0)
                    {
                        Spectrum spec = beta * intersection.Le(wo);
                        L.AddTo(spec);
                    
                    }
                    return L;
                    
                }


                // SPEEDUP 
                // Sample lights, path reuse
                Spectrum lightContribution = beta * Light.UniformSampleOneLight(intersection, s);
                L = L.AddTo(lightContribution);

                //Get new ray direction by sampling the BSDF
                Shape interactionObjShape = (Shape)intersectionObj;
                (Spectrum f, Vector3 wi, double pdf, bool isSpecular) = interactionObjShape.BSDF.Sample_f(wo, intersection);
                if (pdf == 0) { return L; }

                double theta = Math.Abs(Vector3.Dot(wi, intersection.Normal));
                beta = beta * f * theta / pdf;
                r = intersection.SpawnRay(wi);


                // SPEEDUP 2
                // Russian roulette speedup

                int minBounces = 3;
                if (nbounces > minBounces)
                {
                    double q = q_russion_roulette(beta);
                    if (get_random_num() < q) { return L; }
                    beta = beta / (1 - q);
                }

                nbounces += 1;
            }

            return L;
        }

        public double get_random_num()
        {
            return ThreadSafeRandom.NextDouble();
        }
        public double q_russion_roulette(Spectrum beta)
        {
            return Math.Max(0.05, 1 - Math.Max(beta.c[0], Math.Max(beta.c[1], beta.c[2])));
        }

    }


}
