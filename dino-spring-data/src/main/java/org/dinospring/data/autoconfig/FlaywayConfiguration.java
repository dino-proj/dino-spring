// Copyright 2022 dinospring.cn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.dinospring.data.autoconfig;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author tuuboo
 * @date 2022-05-05 02:32:26
 */

@Configuration
@EnableAutoConfiguration(exclude = { FlywayAutoConfiguration.class })
public class FlaywayConfiguration {

  @Bean
  public FlywayConfigurationCustomizer flaywayConfigurationCustomizer() {
    return (config) -> {
      config.baselineOnMigrate(true);
      config.failOnMissingLocations(false);
    };
  }

}
