rootProject.name = "climbing-day"

include("core:domain")
include("core:infra")
include("core:security")
include("api:member")
include("core:infra:testFixtures")
findProject(":core:infra:testFixtures")?.name = "testFixtures"
include("core:infra:testFixtures")
findProject(":core:infra:testFixtures")?.name = "testFixtures"
include("api:common")
findProject(":api:common")?.name = "common"
